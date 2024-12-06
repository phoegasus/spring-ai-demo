package org.phoegasus.springai.part6;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.KeywordMetadataEnricher;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;

@Configuration
public class TextRagConfiguration {

  @Value("classpath:/docs/*")
  private Resource[] docs;

  @Bean
  SimpleVectorStore specStore(EmbeddingModel embeddingModel, ChatModel chatModel) {
    SimpleVectorStore simpleVectorStore = new SimpleVectorStore(embeddingModel);
    File storeFile = storeFile();
    if (storeFile.exists()) {
      simpleVectorStore.load(storeFile);
    } else {
      TokenTextSplitter textSplitter = new TokenTextSplitter();
      KeywordMetadataEnricher keywordMetadataEnricher = new KeywordMetadataEnricher(chatModel, 3);

      Arrays.stream(docs)
              .map(doc -> new TextReader(doc).read())
              .map(keywordMetadataEnricher)
              .map(textSplitter)
              .forEach(simpleVectorStore::add);

      simpleVectorStore.save(storeFile);
    }
    return simpleVectorStore;
  }

  private File storeFile() {
    Path storeFileDir = Path.of("src", "main", "resources", "data");
    String filePath = storeFileDir.toFile().getAbsolutePath() + "/" + "text_store.json";
    return new File(filePath);
  }
}
