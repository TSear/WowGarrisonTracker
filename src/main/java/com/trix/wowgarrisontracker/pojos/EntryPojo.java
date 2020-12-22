package com.trix.wowgarrisontracker.pojos;

import java.time.LocalDate;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
@Data
public class EntryPojo {
    
    private Long id;
    private LocalDate entryDate;
    private int garrisonResources;
    private int warPaint;
    private Long accountCharacterId;

}
