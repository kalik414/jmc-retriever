import java.util.List;
import java.util.Map;

class getPlayer {
    public String uniqueId;
    public String name;
}

class PlayerSkinProperties {
    public long timestamp;
    public String profileId;
    public String profileName;
    public boolean signatureRequired;
    public Map<String, Map<String, Object>> textures;
}

class Player {
    public String username;
    public List<Integer> roles;
    public String skin;
}

class World {
    public String uniqueId;
    public int numberId;
    public Map<String, String> owner;
    public String displayName;
    public int size;
    public int votes;
    public String generatorName;
    public Map<String, Double> spawnPosition;
    public List<Map<String, String>> builders;
    public List<Map<String, String>> developers;
    public List<Map<String, String>> flyers;
    public List<Map<String, String>> whitelist;
    public List<Map<String, String>> blacklist;
    public boolean locked;
    public int time;
    public boolean allowBuild;
    public boolean allowFlight;
    public boolean allowPhysics;
    public String createdTime;
    public boolean published;
    public boolean recommended;
    public String displayItem;
    public List<Map<String, String>> resourcepacks;
    public List<String> categories;
}