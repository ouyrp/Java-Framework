package com.wusong.monitoring.loging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.pattern.MessageConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * @author p14
 */
public class MaxLengthMessageConverter  extends MessageConverter {
    private static int maxLength=5000;

    public static int getMaxLength() {
        return maxLength;
    }

    public static void setMaxLength(int maxLength) {
        MaxLengthMessageConverter.maxLength = maxLength;
    }

    @Override
    public String convert(ILoggingEvent event) {
        if(event.getLevel().levelInt< Level.WARN_INT){
            if(event.getFormattedMessage().length()>getMaxLength()){
                return "log("+event.getFormattedMessage().length()+") truncated to "+getMaxLength()+". "+event.getFormattedMessage().substring(0,getMaxLength());
            }
        }
        return super.convert(event);
    }
}
