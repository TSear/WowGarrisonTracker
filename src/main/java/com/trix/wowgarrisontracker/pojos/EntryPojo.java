package com.trix.wowgarrisontracker.pojos;

import com.trix.wowgarrisontracker.model.AccountCharacter;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;


@Setter
@Getter
@Data
public class EntryPojo {

    private Long id;

    @NotNull(message = "Entry date cannot be empty")
    private LocalDate entryDate;


    @Min(value = 0, message = "Minimum value is 0")
    @Max(value = 10000, message = "Maximum value is 10000")
    private int garrisonResources;

    @Min(value = 0, message = "Minimum value is 0")
    @Max(value = 10000, message = "Maximum value is 10000")
    private int warPaint;
    private Long accountCharacterId;

    @NotBlank(message = "You must insert character name")
    @NotNull(message = "You must insert character name")
    private String characterName;

    @NotNull(message = "Account character cannot be null")
    private AccountCharacter accountCharacter;


    public EntryPojo() {
        this.entryDate = LocalDate.now();
        this.garrisonResources = 0;
        this.warPaint = 0;
        this.characterName = "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EntryPojo entryPojo = (EntryPojo) o;
        return Objects.equals(id, entryPojo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void clean() {
        this.entryDate = LocalDate.now();
        this.garrisonResources = 0;
        this.warPaint = 0;
        this.characterName = "";
        this.accountCharacter = null;
        this.accountCharacterId = null;
    }

}
