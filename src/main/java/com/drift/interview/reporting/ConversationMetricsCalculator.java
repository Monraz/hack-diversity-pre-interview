package com.drift.interview.reporting;
//Edited by Christopher Monraz on 4/16/20

import com.drift.interview.model.Conversation;
import com.drift.interview.model.ConversationResponseMetric;
import com.drift.interview.model.Message;
import java.util.List;

public class ConversationMetricsCalculator {
  public ConversationMetricsCalculator() {}

  /**
   * @return a ConversationResponseMetric object which can be used
   * to power data visualizations on the front end. If the conversation had
   * no answer from a team member, the average response time cannot be
   * calculated. If that is the case, '0' will be the return value given to AverageResponseMs
   */
  ConversationResponseMetric calculateAverageResponseTime(Conversation conversation) {
    List<Message> messages = conversation.getMessages();

    /*responseSum is used to Sum all the time a user
    spent waiting for a team member to answer*/
    long responseSum = 0;

    /* waitTimeUser is used to store the time when the
     end user started waiting for a response*/
    long waitTimeUser = 0;

    /*responseCount is a counter used to know how many
    times a team member answered to a end user*/
    int responseCount = 0;

    /*isUserWaiting is used as a flag to know if the end user
    has sent a message and is waiting for a response.*/
    boolean isUserWaiting = false;

    for( Message message : messages) {
      if( message.isTeamMember()) {
        if(isUserWaiting) {
          responseCount++;
          responseSum += (message.getCreatedAt()-waitTimeUser);
          isUserWaiting = false;
        }
      }else{
        if(!isUserWaiting) {
          isUserWaiting = true;
          waitTimeUser = message.getCreatedAt();
        }
      }
    }

    /*responseMs stores the average response time*/
    long responseMs;

    if(responseCount > 0) {
      responseMs = responseSum / responseCount;
    }else {
      responseMs = 0;
    }

    return ConversationResponseMetric.builder()
        .setConversationId(conversation.getId())
        .setAverageResponseMs(responseMs)
        .build();
  }
}
