package com.cs79_1.interactive_dashboard.Entity;

import com.cs79_1.interactive_dashboard.Enum.UserPreference.UIAppearance;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class UserPreference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Version
    private long version;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Enumerated(EnumType.ORDINAL)
    private UIAppearance appearance;

    public UserPreference() {
    }

    public UserPreference(User user, UIAppearance appearance) {
        this.user = user;
        this.appearance = appearance;
    }
}
