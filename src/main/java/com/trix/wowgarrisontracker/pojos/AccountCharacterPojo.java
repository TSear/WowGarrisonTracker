package com.trix.wowgarrisontracker.pojos;

import java.util.Set;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Setter
@Getter
@Data
public class AccountCharacterPojo {
    
    private Long id;
    private String characterName;
    private Long accountId;
    private Long garrisonResources;
    private Long warPaint;
    private Set<EntryPojo> entries;
}
