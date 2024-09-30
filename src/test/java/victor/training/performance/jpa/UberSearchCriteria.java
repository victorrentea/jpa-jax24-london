package victor.training.performance.jpa;

import lombok.Builder;
import victor.training.performance.jpa.entity.Uber.Status;

@Builder
  public record UberSearchCriteria(String name, Status status, boolean hasPassport) {
  }