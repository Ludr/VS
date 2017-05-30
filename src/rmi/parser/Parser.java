package rmi.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Parser {

	public static void main(String[] args) throws Exception {

		generateStub("ConsumerStub");
		generateStub("ProviderStub");

	}

	private static void generateStub(String objectName) throws FileNotFoundException, IOException, Exception {
		// Read Plain Text Class
		String fileName = "idl/plain_texts/stub_class.txt";
		String plainTextClass = readEntirefile(fileName);

		StringBuffer methodesBuffer = new StringBuffer();
		methodesBuffer.append(generateFunctions("idl/gripper_idl.json"));
		methodesBuffer.append(generateFunctions("idl/vertical_idl.json"));
		methodesBuffer.append(generateFunctions("idl/horizontal_idl.json"));

		String singletonPattern = generateSingleton(objectName);

		String pathName = "rmi.generated";

		System.out.println("Now generating : " + objectName + ".java");
		String classString = String.format(plainTextClass, pathName, objectName, singletonPattern,
				methodesBuffer.toString());

		writeToFile(objectName, classString);
	}

	private static String generateSingleton(String objectName) throws Exception {
		String fileName = "idl/plain_texts/singleton.txt";
		String singletonString = readEntirefile(fileName);

		return String.format(singletonString, objectName, objectName, objectName, objectName);
	}

	private static String generateFunctions(String fileName) throws Exception {
		String jsonText = readEntirefile(fileName);

		fileName = "idl/plain_texts/idl_function.txt";
		String plainTextMethode = readEntirefile(fileName);

		JSONParser parser = new JSONParser();
		JSONObject json = (JSONObject) parser.parse(jsonText);
		JSONArray methodes = (JSONArray) json.get("Functions");

		// Fill Description Arrays
		List<String> methodeNames = new ArrayList<>();
		Map<String, Map<Integer, Map<String, String>>> methodeParameterMap = new HashMap<>();
		Map<String, String> methodeReturnMap = new HashMap<>();

		for (Object obj : methodes) {
			JSONObject jsonObj = (JSONObject) obj;
			String methodeName = (String) jsonObj.get("name");
			methodeNames.add(methodeName);
			JSONArray parameterArray = (JSONArray) jsonObj.get("parameters");
			Map<Integer, Map<String, String>> parameterPositionMap = new HashMap<>();

			for (Object parameterObj : parameterArray) {
				JSONObject jsonParameterObj = (JSONObject) parameterObj;
				HashMap<String, String> parameterDescriptionMap = new HashMap<>();

				Integer positionInteger = Integer.parseInt(jsonParameterObj.get("position").toString());
				parameterDescriptionMap.put("type", (String) jsonParameterObj.get("type"));
				parameterDescriptionMap.put("name", (String) jsonParameterObj.get("name"));

				parameterPositionMap.put(positionInteger, parameterDescriptionMap);
			}
			methodeParameterMap.put(methodeName, parameterPositionMap);
			methodeReturnMap.put(methodeName, (String) jsonObj.get("returnType"));
		}

		// Create Methode Strings and Class String
		StringBuffer parametersBuffer;
		StringBuffer methodesBuffer = new StringBuffer();

		for (String methodeName : methodeNames) {
			parametersBuffer = new StringBuffer();
			Map<Integer, Map<String, String>> parameterPositionMap = methodeParameterMap.get(methodeName);
			int i = 1;
			Map<String, String> parameter = parameterPositionMap.get(new Integer(i++));

			String marshallParam = parameter != null ? parameter.get("name") : "null";

			while (parameter != null) {
				if (i > 2) {
					parametersBuffer.append(", ");
				}

				parametersBuffer.append(parameter.get("type"));
				parametersBuffer.append(" ");
				parametersBuffer.append(parameter.get("name"));

				parameter = parameterPositionMap.get(new Integer(i++));
			}
			String returnType = methodeReturnMap.get(methodeName);
			String returnStatement = "";

			// Here every supported return type has to be listed
			switch (returnType) {
			case "int":
				returnStatement = "return 0;";
				break;
			}

			methodesBuffer.append(String.format(plainTextMethode, returnType, methodeName, parametersBuffer.toString(),
					marshallParam, returnStatement));
		}
		return methodesBuffer.toString();
	}

	private static void writeToFile(String objectName, String classString) throws IOException {
		String fileName;
		fileName = "src/rmi/generated/" + objectName + ".java";
		PrintWriter writer = new PrintWriter(new FileWriter(new File(fileName)));

		writer.print(classString);
		writer.flush();
		writer.close();
	}

	private static String readEntirefile(String fileName) throws FileNotFoundException, IOException {
		BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)));

		String line = "";
		StringBuffer buffer = new StringBuffer();
		while ((line = reader.readLine()) != null) {
			buffer.append(line);
			buffer.append("\n");
		}

		reader.close();
		String jsonText = buffer.toString();
		return jsonText;
	}

}
