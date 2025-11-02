import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Poster {
    static Map <Integer, String> playerRoles = new HashMap<>();
    static Base64.Decoder decoder = Base64.getDecoder();
    static HttpClient client = HttpClient.newHttpClient();
    static Scanner scanner = new Scanner(System.in);
    static ObjectMapper objectMapper = new ObjectMapper();
    public static void main(String[] args) throws JsonProcessingException {
        playerRoles.put(1, "Admin");
        playerRoles.put(2, "Dev");
        playerRoles.put(3, "Sr. Mod");
        playerRoles.put(4, "Mod");
        playerRoles.put(5, "Support");
        playerRoles.put(6, "Nova");
        playerRoles.put(7, "Galaxy");
        playerRoles.put(8, "Star");
        playerRoles.put(9, "Planet");
        playerRoles.put(10, "Moon");
        playerRoles.put(11, "Meteor");
        System.out.println("Use \"help\" to get full list of commands.");
        while (true) {
            System.out.print("$ ");
            String input = scanner.nextLine();
            if (input.startsWith("player ")) {
                String[] player = input.split(" ");
                if (player.length != 2) return;
                player(player[1]);
            } else if (input.startsWith("world ")) {
                String[] id = input.split(" ");
                if (id.length != 2) return;
                world(id[1]);
            } else if (input.equals("help")) {
                System.out.println("""
                                List of commands attached:
                                
                                player [username] - get some info about player as Role and Skin;
                                world [id] - get basically all info about world by its id;
                                help - show this message;
                                exit - properly stop program.""");
            } else if (input.equals("exit")) {
                System.exit(0);
            } else {
                System.out.println("Unknown command.");

            }
        }
    }
    static String removeFormatting(String origin) {
        List<Character> characters = new ArrayList<>();
            for (char c : origin.toCharArray()) characters.add(c);
            while (characters.contains('&')) {
                for (int i = 0; i < characters.size(); i++) {
                    if (characters.get(i) == '&') {
                        if (characters.get(i + 1) == '#') {
                            for (int j = i; j < i + 8; j++) characters.remove(i);
                            // Да я знаю что эту хуйню можно сделать как-то по-умному через List.subList().Clear() но я тупой
                        } else {
                            for (int j = i; j < i + 2; j++) characters.remove(i);
                        }
                    }
                    origin = "";
                    for (char c : characters) origin += c;
                }
            }
        return origin;
    }
    static void player(String name) throws JsonProcessingException {
        System.out.println("Requesting data from server..");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://website.justmc.io/user/"+name))
                .header("Content-Type", "application/json")
                .GET().build();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            System.out.println("Reported exception thrown:");
            throw new RuntimeException(e);
        }
        if (response.statusCode() == 200) {
            String body = response.body();
            Player player;
            try {
                player = objectMapper.readValue(body, Player.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            List<Integer> roles = player.roles;
            String role;
            if (!roles.isEmpty()) {
                int bestRole = roles.getFirst();
                for (int i : roles) if (i<bestRole) bestRole = i;
                role = playerRoles.get(bestRole);
            } else {
                role = "None";
            }
            String encoded = player.skin;
            byte[] decoded = decoder.decode(encoded);
            String output = new String(decoded, StandardCharsets.UTF_8);
            boolean isSkinData = true;
//            System.out.println(output); Этот метод использовался ОЧЕНЬ часто для дебага. Оставил на память.
            Object skinURL = new Object();
                try {
                    PlayerSkinProperties skinProperties = objectMapper.readValue(output, PlayerSkinProperties.class);
                    skinURL = skinProperties.textures.get("SKIN").get("url");
                } catch (MismatchedInputException e) {
                    isSkinData = false;
                }
                System.out.println("\nUsername: "+player.username+"\nRank: "+role);
                if (isSkinData) {
                    System.out.println("Skin URL: " + skinURL);
                } else {
                    System.out.println("Failed to fetch skin data.");
                }
        } else {
            System.out.println("\nFail! Server returned code "+response.statusCode());
        }
        System.out.print("\n");
    }

    static void world(String id) throws JsonProcessingException {
        System.out.println("Requesting data from server..\n");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://api.creative.justmc.io/public/creative/worlds/get/"+id))
                .header("Content-Type", "application/json")
                .GET().build();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (response.statusCode() == 200) {
            World world = objectMapper.readValue(response.body(), World.class);
            Map<String, Double> spawnPos = world.spawnPosition;
            String categories = "";
            for (String i : world.categories) categories += i + " ";
            String resourcepack;
            if (!world.resourcepacks.isEmpty()) {
                resourcepack = world.resourcepacks.get(0).get("url");
            } else {
                resourcepack = "None";
            }
            String axiz = "x: " + spawnPos.get("x") + "; y: " + spawnPos.get("y") + "; z: " + spawnPos.get("z") + "; Pitch: " + spawnPos.get("pitch") + "; Yaw: " + spawnPos.get("yaw") + ".";
            System.out.println("ID: " + world.numberId + "\nOwner: " + world.owner.get("name") + "\nName: " + removeFormatting(world.displayName) + "\nSize: " + world.size +
                    "\nVotes: " + world.votes + "\nGenerator name: " + world.generatorName + "\nSpawn location: " + axiz + "\nBuilders: " + getAllPlayers(world.builders) +
                    "\nDevelopers: " + getAllPlayers(world.developers) + "\nPlayers with fly: " + getAllPlayers(world.flyers) + "\nWhitelisted: " + getAllPlayers(world.whitelist) +
                    "\nBanned: " + getAllPlayers(world.blacklist) + "\nWorld is closed: " + ((world.locked) ? "Yes" : "No") + "\nTime: " + world.time + "\nBuilding is allowed: " +
                    ((world.allowBuild) ? "Yes" : "No") + "\nFlight is allowed: " + ((world.allowFlight) ? "Yes" : "No") + "\nPhysics is allowed: " + ((world.allowPhysics) ? "Yes" : "No") +
                    "\nCreation time: " + world.createdTime + "\nPublished: " + ((world.published) ? "Yes" : "No") + "\nRecommended: " + ((world.recommended) ? "Yes" : "No") +
                    "\nCategories: " + categories + "\nResource pack: " + resourcepack + "\n");
        } else {
            System.out.println("Fail! Server returned code "+response.statusCode()+"\n");
        }
    }
    static String getAllPlayers(List<Map<String, String>> source) {
        String returns = "";
        for (Map<String, String> map : source) returns = returns += map.get("name")+" ";
        return returns;
    }
}