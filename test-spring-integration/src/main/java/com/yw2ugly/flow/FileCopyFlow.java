package com.yw2ugly.flow;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.FileWritingMessageHandler;
import org.springframework.integration.file.filters.SimplePatternFileListFilter;
import org.springframework.integration.file.support.FileExistsMode;
import org.springframework.integration.file.transformer.FileToStringTransformer;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import java.io.File;

/**
 * @author YEWEI
 * @ProjectName Harvey01_springBoot
 * @Description TODO
 * @time 2022/9/1 - 23:06
 */
@Configuration
public class FileCopyFlow {

    private static String INPUT_PATH = "C:\\Users\\yewei\\Desktop\\Harvey";
    private static String OUTPUT_PATH = "C:\\Users\\yewei\\Desktop\\image";

    @Bean
    public MessageChannel fileInputChannel(){
        return  new DirectChannel();
    }


    @Bean
    @InboundChannelAdapter(channel = "fileInputChannel",poller = {@Poller(fixedDelay = "1000")})
    public MessageSource<File> fileReadingMessageSource(){
        FileReadingMessageSource fileReadingMessageSource = new FileReadingMessageSource();
        fileReadingMessageSource.setDirectory(new File(INPUT_PATH));
        fileReadingMessageSource.setFilter(new SimplePatternFileListFilter("*.txt"));
        return fileReadingMessageSource;
    }

    //@Bean
    //@Transformer(inputChannel = "fileInputChannel",outputChannel = "fileOutputChannel")
    public FileToStringTransformer fileToStringTransformer(){
        return new FileToStringTransformer();
    }


    @Bean
    @ServiceActivator(inputChannel = "fileInputChannel")
    public MessageHandler messageHandler(){
        FileWritingMessageHandler fileWritingMessageHandler = new FileWritingMessageHandler(new File(OUTPUT_PATH));
        fileWritingMessageHandler.setExpectReply(false);
        fileWritingMessageHandler.setAppendNewLine(true);
        fileWritingMessageHandler.setFileExistsMode(FileExistsMode.REPLACE);
        return fileWritingMessageHandler;
    }


}
