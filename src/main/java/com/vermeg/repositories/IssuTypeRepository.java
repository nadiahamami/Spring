package com.vermeg.repositories;

import com.vermeg.entities.IssueType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssuTypeRepository extends JpaRepository<IssueType , Long> {

    IssueType findByName(String name);
}
