package GoEuroTest;
import java.io.BufferedInputStream;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class testClass {

	public static void main(String[] args) {
		String cityName = null;
		if(args.length == 0){
			
			BufferedReader br = new BufferedReader( new InputStreamReader(System.in));
			System.out.println("Please Enter the Name of the City: ");
			try {
				cityName = br.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else{
			cityName = args[0];
		}
		if(cityName == null){
			System.out.println("Plese Enter a City Name and Try Again");
		}
		else{
			String url = "http://api.goeuro.com/api/v2/position/suggest/en/" + cityName;
			String response = ApiRequest(url);
			//System.out.println(response);
			if(response == "[]"){
				System.out.println("No Matches Found");
			}
			else if(response ==  ""){
				System.out.println("There was a Problem with the Request, Please try Again");
			}
			else{
				String returnStatement = createCSV(response);
				if(returnStatement == "Done"){
					System.out.println("CSV file Created Successfully");
				}
				else{
					System.out.println("There was a Problem in Creating CSV file");
				}
			}
		}
		
		

	}
	
	
	private static String createCSV(String ApiResult) {
		String result = "";
		try {
			FileWriter writer = new FileWriter("City Details.csv");
			writer.append("_id,Name,Type,Latitude,Longitude\n");
			try{
				JSONArray mainData = new JSONArray(ApiResult);
				for(int i = 0; i < mainData.length(); i++){
					JSONObject city = (JSONObject) mainData.get(i);
					writer.append(city.getString("_id") + ",");
					writer.append(city.getString("name") + ",");
					writer.append(city.getString("type") + ",");
					JSONObject geoPosition = city.getJSONObject("geo_position");
					writer.append(geoPosition.getDouble("latitude") + ",");
					writer.append(geoPosition.getDouble("longitude") + "\n" );
				}
				
			}catch(JSONException e) {
				e.printStackTrace();
			}
			
			writer.flush();
			writer.close();
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		result = "Done";
		
		return result;
	}
	
	private static String ApiRequest(String apiAddress) {
		
		String result = "";
		try {
            URL url = new URL(apiAddress);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            if(connection.getResponseCode() == 200){
                InputStream inp = new BufferedInputStream(connection.getInputStream());
                StringBuffer body = new StringBuffer();
                int ch;
                while ((ch = inp.read()) != -1) {
                    body.append((char) ch);
                }

                result = body.toString();
                inp.close();

            }
            connection.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
		
		return result;
	}

}
