package ra.com.br.miwok;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;

public class PhrasesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view_word);
        loadList();
    }

    private void loadList(){
        ArrayList<Word> words = new ArrayList<>();
        words.add(new Word("Where are you going?", "minto wuksus"));
        words.add(new Word("What is your name?", "tinnә oyaase'nә"));
        words.add(new Word("My name is...", "oyaaset..."));
        words.add(new Word("How are you feeling?", "michәksәs?"));
        words.add(new Word("I’m feeling good", "kuchi achit"));
        words.add(new Word("Are you coming?", "әәnәs'aa?"));
        words.add(new Word("Yes, I’m coming", "hәә’ әәnәm"));
        words.add(new Word("I’m coming", "әәnәm"));
        words.add(new Word("Let’s go", "yoowutis"));
        words.add(new Word("Come here", "әnni'nem"));

        WordAdapter wordAdapter = new WordAdapter(this, words, R.color.category_phrases);
        ListView listViewWords = (ListView) findViewById(R.id.list_view_words);
        listViewWords.setAdapter(wordAdapter);
    }
}
