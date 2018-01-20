package swapp.items.com.swappify.controller.addgame.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class OptionsModel constructor(@SerializedName(value = "id") @Expose val id: Int,
                                    @SerializedName(value = "name") @Expose val name: String)
