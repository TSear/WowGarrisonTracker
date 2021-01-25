package com.trix.wowgarrisontracker.pojos;

import java.time.LocalDate;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@Data
public class EntryPojo {
    
    private Long id;
    private LocalDate entryDate;
    private int garrisonResources;
    private int warPaint;
    private Long accountCharacterId;

    public EntryPojo(){
        this.entryDate = LocalDate.now();
        this.garrisonResources = 0;
        this.warPaint = 0;
    }

}
