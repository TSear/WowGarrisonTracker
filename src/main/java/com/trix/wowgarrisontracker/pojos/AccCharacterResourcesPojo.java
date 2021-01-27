package com.trix.wowgarrisontracker.pojos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AccCharacterResourcesPojo {
    
    private Long id;
    private String characterName;
    private Long warPaint;
    private Long garrisonResources;
    
    public AccCharacterResourcesPojo() {
    	this.warPaint = 0l;
    	this.garrisonResources = 0l;
    }

}
