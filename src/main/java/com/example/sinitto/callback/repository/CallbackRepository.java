package com.example.sinitto.callback.repository;

import com.example.sinitto.callback.entity.Callback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CallbackRepository extends JpaRepository<Callback, Long> {

    Page<Callback> findAll(Pageable pageable);

}
