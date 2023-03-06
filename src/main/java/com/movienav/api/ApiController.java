package com.movienav.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.movienav.domain.entity.Movie;
import com.movienav.domain.repository.movie.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequiredArgsConstructor
public class ApiController {

    private final MovieRepository movieRepository;
    private final Map<Long, Long> MovieIdToTmId;

    private final String api_key = "c1b55d227ffb51efd60981c34af8b23a";

    @GetMapping("api/movie/movies")
    public ResponseEntity<?> addMovies() throws Exception {
        File csv = new File("C:\\Users\\soohy\\moviedata\\movies.csv");
        BufferedReader br = new BufferedReader(new BufferedReader(new FileReader(csv)));

        String line = "";
        boolean skipFirstLine = true;
        while ((line = br.readLine()) != null) {
            if (skipFirstLine) {
                skipFirstLine = false;
                continue;
            }

            //parsing
            String[] token = line.split(",");
            Long movieId = Long.parseLong(token[0]);

            //TMDB api
            Long tmId = MovieIdToTmId.get(movieId);

            if (tmId == null) continue;

            getDetailInfo(tmId, getCastInfo(tmId));
        }

        return ResponseEntity.ok().build();
    }

    private List<String> getCastInfo(Long tmId) {
        List<String> cast_list = null;

        String apiUrl = "https://api.themoviedb.org/3/movie/"+ tmId + "/credits?api_key="+ api_key +"&language=en-ko";

        try {
            URL url = new URL(apiUrl);
            BufferedReader bf;
            bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));

            String result = "";
            result = bf.readLine();

            //parsing
            String match = "[\"]";

            JsonParser jsonParser = new JsonParser();
            JsonObject jsonObject = (JsonObject) jsonParser.parse(result);

            JsonArray casts = (JsonArray) jsonObject.get("cast");
            cast_list = new ArrayList<>();
            for (int i = 0; i < casts.size(); i++) {
                JsonObject object = (JsonObject) casts.get(i);
                String cast_name = object.get("name").toString().replaceAll(match, "");

                cast_list.add(cast_name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cast_list;
    }

    private void getDetailInfo(Long tmId, List<String> cast_list) {
        String apiUrl = "https://api.themoviedb.org/3/movie/"+ tmId + "?api_key="+ api_key +"&language=en-ko";

        try {
            URL url = new URL(apiUrl);
            BufferedReader bf;
            bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));

            String result = "";
            result = bf.readLine();

            //parsing
            String match = "[\"]";
            String ImgUrl = "https://image.tmdb.org/t/p/original";

            JsonParser jsonParser = new JsonParser();
            JsonObject jsonObject = (JsonObject) jsonParser.parse(result);

            JsonArray genres = (JsonArray) jsonObject.get("genres");
            Set<String> genre_list = new HashSet<>();
            for (int i = 0; i < genres.size(); i++) {
                JsonObject object = (JsonObject) genres.get(i);
                String genre_name = object.get("name").toString().replaceAll(match, "");

                genre_list.add(genre_name);
            }

            String str_voteAver = jsonObject.get("vote_average").toString().replaceAll(match, "");

            String str_voteCnt = jsonObject.get("vote_count").toString().replaceAll(match, "");

            String str_runtime = jsonObject.get("runtime").toString().replaceAll(match, "");

            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String str_releaseDate = jsonObject.get("release_date").toString().replaceAll(match, "");

            movieRepository.save(Movie.builder()
                    .tmId(tmId)
                    .title(jsonObject.get("title").toString().replaceAll(match, ""))
                    .poster(ImgUrl + jsonObject.get("poster_path").toString().replaceAll(match, ""))
                    .genreList(genre_list)
                    .castList(cast_list)
                    .overview(jsonObject.get("overview").toString().replaceAll(match, ""))
                    .voteAverage(Double.parseDouble(str_voteAver))
                    .voteCount(Integer.parseInt(str_voteCnt))
                    .goodCount(null)
                    .runtime(Integer.parseInt(str_runtime))
                    .releaseDate(LocalDate.parse(str_releaseDate, dateTimeFormatter))
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //    @GetMapping("api/movie/genres")
//    public ResponseEntity<?> addGenres() {
//        List<String> genres = Arrays.asList(
//                "(no genres listed)", "Mystery", "Adventure", "Horror", "Action", "Romance",
//                "Western", "Film-Noir", "Sci-Fi", "Children", "Animation", "IMAX", "Drama",
//                "Comedy", "Thriller", "Musical", "Fantasy", "Documentary", "War", "Crime");
//
//    }
}
