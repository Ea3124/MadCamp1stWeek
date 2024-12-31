import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView

class GenderAdapter(private val genders: List<String>) : RecyclerView.Adapter<GenderAdapter.GenderViewHolder>() {

    class GenderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val genderButton: Button = view.findViewById(R.id.genderButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.gender_item, parent, false)
        return GenderViewHolder(view)
    }

    override fun onBindViewHolder(holder: GenderViewHolder, position: Int) {
        val gender = genders[position]
        holder.genderButton.text = gender
        holder.genderButton.setOnClickListener {
            // 성별 버튼 클릭 이벤트 처리
            // 예를 들어, 선택된 성별에 따라 다른 액티비티를 시작하거나 상태를 저장할 수 있습니다.
        }
    }

    override fun getItemCount() = genders.size
}
