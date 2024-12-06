package org.phoegasus.springai.bonus;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.evaluation.EvaluationRequest;
import org.springframework.ai.evaluation.EvaluationResponse;
import org.springframework.ai.evaluation.FactCheckingEvaluator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
public class FactCheckingTest {

  @Autowired
  ChatModel chatModel;

  @Test
  void testFactChecking() {
    var factCheckingEvaluator = new FactCheckingEvaluator(ChatClient.builder(chatModel));

    String userPrompt = "What planet is Earth in the order from the Sun?";
    String context = "The Earth is the third planet from the Sun and the only astronomical object known to harbor life.";
    String modelResponse = "The Earth is the fourth planet from the Sun.";

    EvaluationRequest evaluationRequest = new EvaluationRequest(userPrompt, List.of(new Document(context)), modelResponse);

    EvaluationResponse evaluationResponse = factCheckingEvaluator.evaluate(evaluationRequest);

    assertFalse(evaluationResponse.isPass(), "The claim should not be supported by the context");
  }
}
