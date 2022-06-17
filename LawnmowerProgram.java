import static org.junit.Assert.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

public class LawnmowerProgram {

	@Test
	public void testOK() {
	
			try {
				Path tempFile = Files.createTempFile("test-file", ".txt");
			    Files.write(tempFile, "5 5\n1 2 N\nGAGAGAGAA\n3 3 E\nAADAADADDA".getBytes());
			    String result = execute(tempFile);
			    assertEquals(result, "1 3 N 5 1 E");
			    
			} catch (IOException e) {
				System.out.println("File creation error :" + e);
			} catch (Exception e) {
				System.out.println("Execution error :" + e);
			}
	}
	
	@Test
	public void TestGetOut() {
	
			try {
				Path tempFile = Files.createTempFile("test-file", ".txt");
			    Files.write(tempFile, "5 5\n1 2 N\nGAAAAGAAA\n4 4 E\nAAGAAGADAA".getBytes());
			    String result = execute(tempFile);
			    System.out.println(result);
			    assertEquals(result, "0 0 S 4 5 N");    
			} catch (IOException e) {
				System.out.println("File creation error :" + e);
			} catch (Exception e) {
				System.out.println("Execution error :" + e);
			}
	} 
	
	@Test
	public void TestSmashUp() {
	
			try {
				Path tempFile = Files.createTempFile("test-file", ".txt");
			    Files.write(tempFile, "5 5\n1 2 N\nAAAAAGAAA\n1 3 E\nAAGAAGADAA".getBytes());
			    execute(tempFile);
			    fail("Expected smash-up");
			    
			} catch (IOException e) {
				System.out.println("File creation error :" + e);
			} catch (Exception e) {
				System.out.println("Execution error :" + e);
				assertEquals(e.getMessage(), "smash-up");
			}
	}

	private String execute(Path path) throws Exception {
		StringBuilder result = new StringBuilder();
		try {
			List<String> lines = Files.readAllLines(path);
			
			// retrieve the dimensions
			List<Integer>  dimensions = Stream.of(lines.get(0).split(" ")).map (elem -> Integer.parseInt(elem))
				      .collect(Collectors.toList());;
			
			// retrieve the position and the instructions to execute
			
			// get grass Lawn mower 1 data
			String[] pos1 = lines.get(1).split(" ");
			char[] mov1 = lines.get(2).toCharArray();
			
			
			// get grass Lawn mower 2 data
			String[] pos2 = lines.get(3).split(" ");
			char[] mov2 = lines.get(4).toCharArray();
			
			// execute instructions
			move(dimensions, pos1, mov1, pos2);
			move(dimensions, pos2, mov2, pos1);
			
			// build the result
			 for (int i=0; i< pos1.length; i++) {
			     result.append(pos1[i] + " ");
			 }
			 for (int i=0; i< pos2.length; i++) {
			     result.append(pos2[i] + " ");
			 }
			 System.out.println("result :" + result);
			 
			 
			
		} catch (IOException e) {
			System.out.println("erreur de lecture du fichier :" + e);
		}
		return result.toString().trim();
	}



	private void move(List<Integer> dimensions, String[] position, char[] instructionsTab, String[] position2) throws Exception {
		for(char movingCaracter : instructionsTab ) {
			switch(movingCaracter) {
				case 'G' :
					switch(position[2]) {
						case "N" :
							System.out.println(position[2]);
							position[2]="W";
							System.out.println(position[2]);
							break;
						case "W" :
							position[2]="S";
							break;
						case "S" :
							position[2]="E";
							break;
						case "E" :
							position[2]="N";
							break;
					}
					break;
				case 'D' :
					switch(position[2]) {
					case "N" :
						position[2]="E";
						break;
					case "E" :
						position[2]="S";
						break;
					case "S" :
						position[2]="W";
						break;
					case "W" :
						position[2]="N";
						break;
					}
					break;
				case 'A' :
					switch(position[2]) {
					case "N" :
						position[1]=String.valueOf(Integer.parseInt(position[1]) + 1);
						break;
					case "E" :
						position[0]=String.valueOf(Integer.parseInt(position[0]) + 1);
						break;
					case "S" :
						position[1]=String.valueOf(Integer.parseInt(position[1]) - 1);
						break;
					case "W" :
						position[0]=String.valueOf(Integer.parseInt(position[0]) - 1);
						break;
					}
					break;
				}
			
			// handle error smash-up
			if(position[0].equals(position2[0]) && position[1].equals(position2[1])) {
				throw new Exception("smash-up");
			}
			
			// correct position if get out	
			if( Integer.parseInt(position[0]) < 0) {
				position[0] = "0";
			}
			if( Integer.parseInt(position[1]) < 0) {
				position[1] = "0";
			}
			if( Integer.parseInt(position[0]) > dimensions.get(0)) {
				position[0] = Integer.toString(dimensions.get(0));
			}
			if( Integer.parseInt(position[1]) > dimensions.get(0)) {
				position[1] = Integer.toString(dimensions.get(1));;
			}
		
		}
	}
}
