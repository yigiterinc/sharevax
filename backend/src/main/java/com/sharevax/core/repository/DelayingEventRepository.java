package com.sharevax.core.repository;

import com.sharevax.core.model.event.DelayingEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DelayingEventRepository extends JpaRepository<DelayingEvent, Long> {

}
