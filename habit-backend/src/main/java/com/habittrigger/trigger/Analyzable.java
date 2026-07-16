package com.habittrigger.trigger;

import com.habittrigger.dto.InsightResult;
import com.habittrigger.model.HabitLog;

import java.util.List;

/**
 * Interface for any service that can analyze habit logs and produce insights.
 *
 * InsightService implements this, satisfying the "interface" requirement
 * alongside the abstract class hierarchy.
 *
 * Using an interface here is justified: in future you could swap in an
 * ML-based analyzer, a rule-based analyzer, or a remote analytics service —
 * all without changing the controller that calls analyze().
 */
public interface Analyzable {
    InsightResult analyze(List<HabitLog> logs);
}
