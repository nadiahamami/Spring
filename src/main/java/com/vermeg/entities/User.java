package com.vermeg.entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long  id;
    @Column
    private String firstName;
    @Column
    private String lastName;
    @Column
    private ERole post;

    @Column(unique=true)
    private String email;
    @Column
    private String password;
    @Column
    private String avatar;

    @Setter(value = AccessLevel.NONE)
    @Basic(optional = false)
    @CreationTimestamp
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();

    @Setter(value = AccessLevel.NONE)
    @UpdateTimestamp
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt = new Date();

    @ManyToOne
    private Role role ;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "project_user", joinColumns = {
            @JoinColumn(name = "user_id", referencedColumnName ="id") }, inverseJoinColumns = {
            @JoinColumn(name = "project_id", referencedColumnName = "id") })
    private List<Project> projects;


    @ManyToOne
    @JsonBackReference
    private User responsable;

    @OneToMany
    @JsonManagedReference
    private List<User> children ;
}
