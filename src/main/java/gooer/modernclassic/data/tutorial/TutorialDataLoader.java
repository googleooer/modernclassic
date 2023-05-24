package gooer.modernclassic.data.tutorial;

import com.google.gson.*;
import gooer.modernclassic.Modernclassic;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TutorialDataLoader implements SimpleSynchronousResourceReloadListener {
    // A map to store your tutorials by id
    private final Map<Identifier, TutorialData> tutorials = new HashMap<>();


    public TutorialGroup oldGetTutorial(Identifier id) {
        Identifier adjustedId = new Identifier("modernclassic", ("tutorials/"+id.getPath()+".json"));
        TutorialData targetTutorial = tutorials.get(adjustedId);
        //TutorialGroup returnedTutorial = new TutorialGroup(new ArrayList<TutorialStep>(),targetTutorial.getTutorialId());

        return new TutorialGroup(new ArrayList<TutorialStep>(targetTutorial.getSteps()),targetTutorial.getTutorialId());
    }

    // A getter method for other classes to access your tutorials
    public TutorialGroup getTutorial(Identifier id) {
        Identifier adjustedId = new Identifier("modernclassic", ("tutorials/"+id.getPath()+".json"));
        //Modernclassic.LOGGER.info(String.format("TutorialDataLoader: Getting tutorial with ID %s", adjustedId.toString()));
        TutorialData originalTutorial = tutorials.get(adjustedId);
        if (originalTutorial == null) {
            return null; // Tutorial not found
        }

        // Create a deep copy of the tutorial
        List<TutorialStep> copiedSteps = new ArrayList<>();
        for (TutorialStep originalStep : originalTutorial.getSteps()) {
            TutorialStep copiedStep = new TutorialStep(
                    originalStep.getTutorialTitle(),
                    originalStep.getTutorialText(),
                    originalStep.getDuration()//,
                    //originalStep.getAction()
            );
            copiedSteps.add(copiedStep);
        }

        return new TutorialGroup(copiedSteps, originalTutorial.getTutorialId());
    }

    // The identifier of your listener
    @Override
    public Identifier getFabricId() {
        return new Identifier("modernclassic", "tutorials");
    }

    // The method that loads and processes your data
    @Override
    public void reload(ResourceManager manager) {
        // Clear the previous data
        tutorials.clear();

        // Find all the files in the tutorials folder of all data packs
        Map<Identifier, Resource> resources = manager.findResources("tutorials", path -> path.getPath().endsWith(".json"));

        Gson gson = new Gson();

        // Loop through each file and parse it as a TutorialData object
        for (Identifier id : resources.keySet()) {
            try (InputStream stream = manager.getResource(id).get().getInputStream()) {
                // Read the input stream as a Json object
                JsonObject json = new JsonParser().parse(new InputStreamReader(stream)).getAsJsonObject();

                // Create a TutorialData object from the Json object
                TutorialData tutorial = gson.fromJson(json, TutorialData.class);
                //Modernclassic.LOGGER.info(String.format("Loaded tutorial %s with first step title %s and text %s", id.getPath(), tutorial.getSteps().get(0).getTutorialTitle(), tutorial.getSteps().get(0).getTutorialText()));

                // Put the tutorial in the map with its id
                tutorials.put(id, new TutorialData(tutorial.getSteps(), removePathAndExtension(id.toString())));

                //Modernclassic.LOGGER.info(String.format("Added tutorial to map, with %s", tutorials.keySet().toString()));
                //Modernclassic.LOGGER.info(String.format("Added tutorial to map, with id %s", removePathAndExtension(id.toString())));
            } catch (IOException | JsonParseException e) {
                // Handle any exceptions
                e.printStackTrace();
            }
        }

        Modernclassic.LOGGER.info(String.format("Loaded %d tutorials.", resources.size()));

        if(resources.isEmpty()) Modernclassic.LOGGER.warn("No tutorials found!");
    }

    public String removePathAndExtension(String identifierString) {
        int pathSeparatorIndex = identifierString.lastIndexOf('/');
        int extensionIndex = identifierString.lastIndexOf('.');
        int namespaceSeparatorIndex = identifierString.indexOf(':');

        if (pathSeparatorIndex != -1 && extensionIndex != -1 && extensionIndex > pathSeparatorIndex) {
            String namespace = identifierString.substring(0, namespaceSeparatorIndex + 1);
            String tutorialName = identifierString.substring(pathSeparatorIndex + 1, extensionIndex);
            return namespace + tutorialName;
        }

        return identifierString;
    }
}