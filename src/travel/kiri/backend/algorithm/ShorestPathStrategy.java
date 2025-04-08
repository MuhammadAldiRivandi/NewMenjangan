package travel.kiri.backend.algorithm;

import java.util.Set;

public abstract class ShorestPathStrategy {
    public abstract double runAlgorithm(Set<String> trackTypeIdBlacklist);

    public abstract int getParent(int node);

    public abstract double getDistance(int node);
}