import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.barkepedia.R
import com.example.barkepedia.model.DogBreed

class DogAdapter(
    private var dogs: List<DogBreed>,
    private val onItemClick: (DogBreed) -> Unit
) : RecyclerView.Adapter<DogAdapter.DogViewHolder>() {

    class DogViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var dogImage: ImageView = view.findViewById(R.id.imageViewDog)
        val dogName: TextView = view.findViewById(R.id.textViewDogName)
        val breedGroup: TextView = view.findViewById(R.id.textViewBreedGroup)
        val imageProgress: ProgressBar = view.findViewById(R.id.imageProgressBar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_item_dog, parent, false)
        return DogViewHolder(view)
    }

    override fun onBindViewHolder(holder: DogViewHolder, position: Int) {
        val dog = dogs[position]

        holder.dogName.text = dog.name
        holder.breedGroup.text = dog.breedGroup ?: "Unknown"

        // Show progress bar before loading image
        holder.imageProgress.visibility = View.VISIBLE

        val imageUrl = "https://cdn2.thedogapi.com/images/${dog.referenceImageId}.jpg"
        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .placeholder(R.drawable.ic_launcher_background)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    holder.imageProgress.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>?,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    holder.imageProgress.visibility = View.GONE
                    return false
                }
            })
            .into(holder.dogImage)

        holder.itemView.setOnClickListener {
            onItemClick(dog)
        }
    }

    override fun getItemCount() = dogs.size

    fun updateData(newDogs: List<DogBreed>) {
        dogs = newDogs
        notifyDataSetChanged()
    }
}