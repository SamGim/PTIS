package com.thewayhome.ptis.core.repository;

import com.thewayhome.ptis.core.vo.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, String> {
}
