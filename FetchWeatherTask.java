public class FetchWeatherTask extends AsyncTask<String,Void,Void>{

        @Override
        protected Void doInBackground(String... params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;

            String countryCode=params[0];
            int daysCount=7;


            try {
                final String COUNTRY_CODE="q";
                final String MODE="mode";
                final String UNIT="units";
                final String DAYS_COUNT="cnt";
                final String API_KEY="APPID";

                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                Uri.Builder builder=new Uri.Builder();
                builder.scheme("http")
                        .authority("api.openweathermap.org")
                        .path("data/2.5/forecast/daily")
                        .appendQueryParameter(COUNTRY_CODE, countryCode)
                        .appendQueryParameter(MODE, "json")
                        .appendQueryParameter(UNIT,"metric")
                        .appendQueryParameter(DAYS_COUNT,Integer.toString(daysCount))
                        .appendQueryParameter(API_KEY,"aed05dae3c7ca04436648f635661e3dc");
                String stringUrl=builder.build().toString();
                Log.v("built URL",stringUrl);
                URL url = new URL(stringUrl);

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;

                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");

                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                forecastJsonStr = buffer.toString();
                Log.v(LOG_TAG,"Weather Data: "+forecastJsonStr);
            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }
            return null;
        }
    }
