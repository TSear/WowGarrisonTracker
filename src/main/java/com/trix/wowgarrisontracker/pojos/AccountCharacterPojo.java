package com.trix.wowgarrisontracker.pojos;

import java.util.HashSet;
import java.util.Set;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;


@Setter
@Getter
@Data
public class AccountCharacterPojo {
    
    private Long id;
    @Size(max = 100)
    private String characterName;
    private Long accountId;
    private Long garrisonResources;
    private Long warPaint;
    private Set<EntryPojo> entries;

    public AccountCharacterPojo(){
        this.characterName = "";
        this.garrisonResources = 0l;
        this.warPaint = 0l;
        this.entries = new HashSet<>();
    }
}
