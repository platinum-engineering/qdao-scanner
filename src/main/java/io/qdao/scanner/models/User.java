package io.qdao.scanner.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.qdao.scanner.utils.Utils;
import io.qdao.scanner.types.Role;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column
    private String displayName;

    @Column
    private String email;

    @Column
    private String password;

    @Column(name = "roles", columnDefinition = "app_roles[]")
    @Type(type = "io.qdao.scanner.types.usertypes.RoleArrayType")
    private List<Role> roles = new ArrayList<>();

    @Column
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Utils.DATA_ISO_8601_PATTERN)
    private Date createdAt;

    @Column
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Utils.DATA_ISO_8601_PATTERN)
    private Date updatedAt;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
