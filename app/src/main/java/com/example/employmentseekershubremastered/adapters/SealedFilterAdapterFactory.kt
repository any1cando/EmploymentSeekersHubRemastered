package com.example.employmentseekershubremastered.adapters

import android.util.Log
import com.example.employmentseekershubremastered.model.dto.main.filters.CheckBoxDto
import com.example.employmentseekershubremastered.model.dto.main.filters.RangeDto
import com.example.employmentseekershubremastered.model.dto.main.filters.SealedFilterDto
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter

class SealedFilterAdapterFactory : TypeAdapterFactory {
    override fun <T> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T>? {
        val rawType = type.rawType
        if (rawType != SealedFilterDto::class.java) {
            return null
        }

        val checkBoxAdapter = gson.getDelegateAdapter(this, TypeToken.get(SealedFilterDto.CheckBoxFilterDto::class.java))
        val rangeAdapter = gson.getDelegateAdapter(this, TypeToken.get(SealedFilterDto.RangeFilterDto::class.java))
        val searchAdapter = gson.getDelegateAdapter(this, TypeToken.get(SealedFilterDto.SearchFilterDto::class.java))

        return object : TypeAdapter<T>() {
            override fun write(out: JsonWriter, value: T) {
                when (value) {
                    is SealedFilterDto.CheckBoxFilterDto -> checkBoxAdapter.write(out, value)
                    is SealedFilterDto.RangeFilterDto -> rangeAdapter.write(out, value)
                    is SealedFilterDto.SearchFilterDto -> searchAdapter.write(out, value)
                }
            }

            override fun read(reader: JsonReader): T {
                reader.beginObject()
                var type: String? = null
                var result: T? = null

                Log.i("JsonReader", "Starting to read the JSON object")

                while (reader.hasNext()) {
                    val name = reader.nextName()
                    Log.i("JsonReader", "Reading property: $name")

                    if (name == "type") {
                        type = reader.nextString()
                        Log.i("FilterType", "Received filter type: $type")
                    } else if (name == "data") {
                        Log.i("JsonReader", "Processing 'data' field for type: $type")

                        result = when (type) {
                            "checkBox" -> {
                                if (reader.peek() == JsonToken.BEGIN_ARRAY) {
                                    Log.i("JsonReader", "Using CheckBoxAdapter to read the data array")
                                    val checkBoxList = mutableListOf<CheckBoxDto>()
                                    reader.beginArray()
                                    while (reader.hasNext()) {
                                        checkBoxList.add(gson.fromJson(reader, CheckBoxDto::class.java))
                                    }
                                    reader.endArray()
                                    SealedFilterDto.CheckBoxFilterDto(type, checkBoxList) as T
                                } else {
                                    Log.e("JsonReader", "Expected BEGIN_ARRAY for checkBox data but was ${reader.peek()}")
                                    throw JsonParseException("Expected BEGIN_ARRAY for checkBox data but was ${reader.peek()}")
                                }
                            }
                            "range" -> {
                                Log.i("JsonReader", "Using RangeAdapter to read the data")
                                val rangeData = gson.fromJson<RangeDto>(reader, RangeDto::class.java)
                                SealedFilterDto.RangeFilterDto(type, rangeData) as T
                            }
                            "search" -> {
                                Log.i("JsonReader", "Using SearchAdapter to read the data")
                                searchAdapter.read(reader) as T
                            }
                            else -> {
                                Log.e("JsonReader", "Unknown filter type encountered: $type")
                                throw JsonParseException("Unknown filter type: $type")
                            }
                        }
                    } else {
                        Log.w("JsonReader", "Skipping unknown property: $name")
                        reader.skipValue()
                    }
                }

                reader.endObject()

                Log.i("JsonReader", "Finished reading the JSON object")

                return result ?: throw JsonParseException("Failed to parse JSON to known filter type")
            }
        }
    }
}
