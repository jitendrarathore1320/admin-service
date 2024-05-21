package com.advantal.adminRoleModuleService.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.advantal.adminRoleModuleService.models.Admin;
import com.advantal.adminRoleModuleService.models.News;



@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

	News findByIdAndStatus(Long id, Short one);

	ArrayList<News> findAllByStatusOrderByCreationDateDesc(Short one);

//	@Query(value = "SELECT * FROM news ad WHERE ad.status != 0", countQuery = "SELECT count(*) from admin ad WHERE ad.status != 0", nativeQuery = true)
//	Page<News> findAllNews(String keyWord, Pageable pageable);
//	
	@Query(value = "SELECT * FROM news ne WHERE ne.status != 0 and (ne.sub_title like concat('%',?1,'%') or ne.title like concat('%',?1,'%') or ne.description like concat('%',?1,'%'))", countQuery = "SELECT count(*) from news ne WHERE ne.status != 0 and (ne.sub_title like concat('%',?1,'%') or ne.title like concat('%',?1,'%') or ne.description like concat('%',?1,'%'))", nativeQuery = true)
	Page<News> findAllNews(String keyWord, Pageable pageable);

	@Query(value = "SELECT * FROM news ne WHERE ne.status != 0", countQuery = "SELECT count(*) from news ne WHERE ne.status != 0", nativeQuery = true)
	Page<News> findAllNews(Pageable pageable);

	@Query(value = "SELECT * FROM news ne WHERE ne.status != 0 and (ne.sub_title like concat('%',?1,'%') or ne.title like concat('%',?1,'%') or ne.description like concat('%',?1,'%'))", countQuery = "SELECT count(*) from news ne WHERE ne.status != 0 and (ne.sub_title like concat('%',?1,'%') or ne.title like concat('%',?1,'%') or ne.description like concat('%',?1,'%'))", nativeQuery = true)
	List<News> findAllNewsWithSeraching(String keyWord);

	@Query(value = "SELECT * FROM news ne WHERE ne.status != 0", countQuery = "SELECT count(*) from news ne WHERE ne.status != 0", nativeQuery = true)
	List<News> findAllNews();
	



}