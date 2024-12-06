package org.phoegasus.springai.part7;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/expertPlusPlus")
class ExpertPlusPlusController {

  private final ChatClient chatClient;
  private final VectorStore specStore;
  private final VectorStore codeStore;

  @Value("classpath:/prompts/expert/prompt.st")
  private Resource promptResource;

  public ExpertPlusPlusController(ChatClient chatClient, @Qualifier("specStore") VectorStore specStore, @Qualifier("codeStore") VectorStore codeStore) {
    this.chatClient = chatClient;
    this.specStore = specStore;
    this.codeStore = codeStore;
  }

  @GetMapping
  public String ask(@RequestParam("prompt") String userPrompt) {
    List<Document> similarSpecDocs = specStore.similaritySearch(SearchRequest.query(userPrompt).withTopK(4));
    List<Document> similarCodeDocs = codeStore.similaritySearch(SearchRequest.query(userPrompt).withTopK(2));
    List<Document> similarDocs = new ArrayList<>();
    similarDocs.addAll(similarSpecDocs);
    similarDocs.addAll(similarCodeDocs);
    List<String> contentList = similarDocs.stream().map(Document::getContent).toList();

    PromptTemplate promptTemplate = new PromptTemplate(promptResource);

    Map<String, Object> params = new HashMap<>();
    params.put("input", userPrompt);
    params.put("documents", contentList);

    Prompt prompt = promptTemplate.create(params);

    return chatClient.prompt(prompt).call().content();
  }
}
