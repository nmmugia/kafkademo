package com.gitlab.seemugia1.kafkademo;

public class InsurancePolicyEvent {
    public String action;
    public InsurancePolicy policy;

    public InsurancePolicyEvent() {
    }

    public String getAction() {
        return action;
    }

    public InsurancePolicy getPolicy() {
        return policy;
    }

    public InsurancePolicyEvent(String action, InsurancePolicy policy) {
        this.action = action;
        this.policy = policy;
    }
}
