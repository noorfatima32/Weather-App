import android.content.Context
import java.util.Properties

fun getApiKey(context: Context): String {
    val properties = Properties()
    val inputStream = context.assets.open("local.properties")
    properties.load(inputStream)
    return properties.getProperty("WEATHER_API_KEY")
}
