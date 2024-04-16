package org.zerock.board.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.board.dto.MovieDTO;
import org.zerock.board.dto.PageRequestDTO;
import org.zerock.board.dto.PageResultDTO;
import org.zerock.board.entity.Movie;
import org.zerock.board.entity.MovieImage;
import org.zerock.board.repository.MovieImageRepository;
import org.zerock.board.repository.MovieRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService{

    private final MovieRepository movieRepository;

    private final MovieImageRepository imageRepository;


    @Transactional
    @Override
    public Long register(MovieDTO movieDTO) {

        //Map<String,Object>

        Map<String,Object> entityMap = dtoToEntity(movieDTO);
        Movie movie = (Movie)entityMap.get("movie");
        List<MovieImage> movieImageList = (List<MovieImage>) entityMap.get("imgList");

        movieRepository.save(movie);

        movieImageList.forEach(movieImage -> {

            imageRepository.save(movieImage);
        });

        return movie.getMno();
    }

    @Override
    public PageResultDTO<MovieDTO, Object[]> getList(PageRequestDTO requestDTO) {

        Pageable pageable = requestDTO.getPageable(Sort.by("mno").descending());
        Page<Object[]> result = movieRepository.getListPage(pageable);

        log.info("===================getList===========================");
        result.getContent().forEach(arr ->{
               //Object[] 하나 받음 Object[] 안에는 entity객체 있음
            Movie mo = (Movie)arr[0];
            log.info(mo.getTitle());
            log.info(Arrays.toString(arr));
        });

        log.info("==============================================");


        Function<Object[], MovieDTO> fn = (arr -> entitiesToDTO(
                (Movie)arr[0],
                (List<MovieImage>)(Arrays.asList((MovieImage)arr[1])),
                (Double) arr[2],
                (Long)arr[3])
        );

        return new PageResultDTO<>(result, fn);
    }

    @Override
    public MovieDTO getMovie(Long mno) {

        List<Object[]> result = movieRepository.getMovieWithAll(mno);
                                //result 리스트에 0번째 위치한 Object[]에 0번째 요소
        Movie movie = (Movie) result.get(0)[0]; //Movie엔티티는 가장 앞에 존재 -모든 row가 동일한 값

        List<MovieImage> movieImageList = new ArrayList<>(); //영화의 이미지 개수만큼 MovieImage객체 필요

        result.forEach(arr ->{
            //arr -> Object[]
            MovieImage movieImage = (MovieImage) arr[1];
            movieImageList.add(movieImage);
        });

        Double avg = (Double) result.get(0)[2]; //평균 평점 - 모든 Row가 동일한 값
        Long reviewCnt = (Long) result.get(0)[3]; //리뷰 개수 - 모든 row가 동일한 값

        return entitiesToDTO(movie,movieImageList,avg,reviewCnt);
    }


}
