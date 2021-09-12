package main.model.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="captcha_codes")
public class Captcha
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private long time;

    @Column(nullable = false, columnDefinition = "TINYTEXT")
    private String code;

    @Column(name="secret_code", nullable = false, columnDefinition = "TINYTEXT")
    private String secretCode;
}
