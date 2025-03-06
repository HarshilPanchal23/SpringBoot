package com.demo.sqs.entity;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    private String id;
    private String content;
    private Date createdAt;
}