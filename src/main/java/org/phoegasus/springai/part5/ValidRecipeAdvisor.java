package org.phoegasus.springai.part5;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.advisor.api.AdvisedRequest;
import org.springframework.ai.chat.client.advisor.api.AdvisedResponse;
import org.springframework.ai.chat.client.advisor.api.CallAroundAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAroundAdvisorChain;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ValidRecipeAdvisor implements CallAroundAdvisor {

  private final ObjectMapper objectMapper;

  @Override
  public AdvisedResponse aroundCall(AdvisedRequest request, CallAroundAdvisorChain chain) {
    AdvisedRequest advisedRequest = adviseRequest(request);

    AdvisedResponse response = chain.nextAroundCall(advisedRequest);

    if (!isRecipeValid(response)) {
      return advisedResponseWithInvalidRecipe(response);
    }

    return response;
  }

  private AdvisedRequest adviseRequest(AdvisedRequest request) {
    String advicePrompt = """
            If the ingredient is valid and you manage to produce a valid recipe with it, set the valid attribute to true.
            If the ingredient is invalid, or you are unable to produce a valid recipe, set the valid attribute
            to false, and all the other attributes to null or zero.
            """;
    String advisedSystemText = request.systemText() + System.lineSeparator() + advicePrompt;
    return AdvisedRequest.from(request).withSystemText(advisedSystemText).build();
  }

  private boolean isRecipeValid(AdvisedResponse response) {
    try {
      String responseContent = response.response().getResult().getOutput().getContent();
      Recipe recipe = objectMapper.readValue(responseContent, Recipe.class);
      return recipe.isValid();
    } catch (Exception e) {
      log.error("Could not validate recipe", e);
    }
    return false;
  }

  private AdvisedResponse advisedResponseWithInvalidRecipe(AdvisedResponse response) {
    try {
      return AdvisedResponse.from(response)
              .withResponse(ChatResponse.builder().from(response.response())
                      .withGenerations(
                              List.of(
                                      new Generation(
                                              new AssistantMessage(objectMapper.writeValueAsString(Recipe.invalid()))
                                      )
                              )
                      ).build()
              ).build();
    } catch (Exception e) {
      log.error("Could not create AdvisedResponse", e);
    }
    return null;
  }

  @Override
  public String getName() {
    return getClass().getName();
  }

  @Override
  public int getOrder() {
    return LOWEST_PRECEDENCE;
  }
}
