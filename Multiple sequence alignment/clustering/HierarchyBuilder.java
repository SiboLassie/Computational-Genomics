/**
 * Computational-Genomics 
 * Part 3 - Multiple sequence alignment.
 */

package clustering;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class HierarchyBuilder {

    private DistanceMap distances;
    private List<Cluster> clusters;

    public DistanceMap getDistances() {
        return distances;
    }

    public List<Cluster> getClusters() {
        return clusters;
    }

    public HierarchyBuilder(List<Cluster> clusters, DistanceMap distances) {
        this.clusters = clusters;
        this.distances = distances;
    }

    /**
     * Returns Flattened clusters, i.e. clusters that are at least apart by a given threshold
     * @param linkageStrategy
     * @param threshold
     * @return
     */
    public List<Cluster> flatAgg(LinkageStrategy linkageStrategy, Double threshold)
    {
        while((!isTreeComplete()) && (distances.minDist() != null) && (distances.minDist() <= threshold))
        {
            agglomerate(linkageStrategy);
        }

        return clusters;
    }

    public void agglomerate(LinkageStrategy linkageStrategy) {
        ClusterPair minDistLink = distances.removeFirst();
        if (minDistLink != null) {
            clusters.remove(minDistLink.getrCluster());
            clusters.remove(minDistLink.getlCluster());

            Cluster oldClusterL = minDistLink.getlCluster();
            Cluster oldClusterR = minDistLink.getrCluster();
            Cluster newCluster = minDistLink.agglomerate(null);

            for (Cluster iClust : clusters) {
                ClusterPair link1 = findByClusters(iClust, oldClusterL);
                ClusterPair link2 = findByClusters(iClust, oldClusterR);
                ClusterPair newLinkage = new ClusterPair();
                newLinkage.setlCluster(iClust);
                newLinkage.setrCluster(newCluster);
                Collection<Distance> distanceValues = new ArrayList<Distance>();

                if (link1 != null) {
					Double distVal = link1.getLinkageDistance();
                    Double weightVal = link1.getOtherCluster(iClust).getWeightValue();
                    distanceValues.add(new Distance(distVal, weightVal));
                    distances.remove(link1);
                }
                if (link2 != null) {
					Double distVal = link2.getLinkageDistance();
					Double weightVal = link2.getOtherCluster(iClust).getWeightValue();
                    distanceValues.add(new Distance(distVal, weightVal));
                    distances.remove(link2);
                }

                Distance newDistance = linkageStrategy.calculateDistance(distanceValues);

				newLinkage.setLinkageDistance(newDistance.getDistance());
                distances.add(newLinkage);

            }
            clusters.add(newCluster);
        }
    }

    private ClusterPair findByClusters(Cluster c1, Cluster c2) {
        return distances.findByCodePair(c1, c2);
    }

    public boolean isTreeComplete() {
        return clusters.size() == 1;
    }

    public Cluster getRootCluster() {
        if (!isTreeComplete()) {
            throw new RuntimeException("No root available");
        }
        return clusters.get(0);
    }

}
