package org.abc.app.device;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Table(name = "device")
public class Device {

    @Id
    @GeneratedValue
    private Long id;

    @CreationTimestamp
    private Date createdAt = new Date();

    @UpdateTimestamp
    private Date updatedAt = new Date();

    @Column(length = 50)
    public String name;

    @Column(length = 50)
    public String brand;
}
