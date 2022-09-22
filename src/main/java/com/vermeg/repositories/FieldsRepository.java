package com.vermeg.repositories;

import com.vermeg.entities.Fields;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FieldsRepository extends JpaRepository<Fields , Long> {

    Fields findByFiedlsKey (String name );
}
