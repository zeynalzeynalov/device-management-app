package org.abc.app.device;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.stereotype.Service;

import java.util.Date;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "device")
public class Device {

    @JsonProperty("id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("name")
    @Column(length = 50)
    private String name;

    @JsonProperty("brand")
    @Column(length = 50)
    private String brand;

    @JsonProperty("createdAt")
    @CreationTimestamp
    private Date createdAt = new Date();

    @JsonProperty("updatedAt")
    @UpdateTimestamp
    private Date updatedAt = new Date();
}
