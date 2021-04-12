package com.trix.wowgarrisontracker.converters;

import com.trix.wowgarrisontracker.model.Options;
import com.trix.wowgarrisontracker.pojos.OptionsDTO;
import org.springframework.core.convert.converter.Converter;

public class OptionsToOptionsDTO implements Converter<Options, OptionsDTO> {

    @Override
    public OptionsDTO convert(Options options) {
        OptionsDTO optionsDTO = new OptionsDTO();
        optionsDTO.setId(options.getId());
        optionsDTO.setAccountId(options.getAccount().getId());
        optionsDTO.setServer(options.getServer());
        optionsDTO.setReceiveEmailNotifications(options.isReceiveEmailNotifications());
        return optionsDTO;
    }
}
