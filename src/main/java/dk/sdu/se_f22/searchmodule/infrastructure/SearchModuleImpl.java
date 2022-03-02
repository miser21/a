package dk.sdu.se_f22.searchmodule.infrastructure;

import dk.sdu.se_f22.searchmodule.infrastructure.interfaces.Indexable;
import dk.sdu.se_f22.sharedlibrary.models.Brand;

import java.lang.reflect.Type;
import java.util.*;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SearchModuleImpl implements SearchModule {
    private final Set<Indexable<?>> indexes;

    public SearchModuleImpl() {
        this.indexes = new HashSet<>();
    }

    public <T extends Indexable<?>> void addIndex(T index) {
        indexes.add(index);
    }

    public <T extends Indexable<?>> void removeIndex(T index) {
        indexes.remove(index);
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> queryIndexOfType(Class<T> clazz, List<String> tokens) {
        BiFunction<Type, String, Boolean> interfaceGenericEquals = (Type genericInterface, String target) -> {
            Pattern pattern = Pattern.compile("<([^>]+)>");
            Matcher matcher = pattern.matcher(genericInterface.getTypeName());
            return matcher.find() && Objects.equals(matcher.group(1), target);
        };

        for (var index : indexes)
            for (Type genericInterface : index.getClass().getGenericInterfaces())
                if (interfaceGenericEquals.apply(genericInterface, clazz.getTypeName()))
                    return (List<T>) index.queryIndex(tokens);

        throw new NoSuchElementException();
    }

    @Override
    public SearchResult search(String query) {
        SearchResult searchResult = new SearchResult(/*new ArrayList<Content>(), new ArrayList<Product>(),*/ new ArrayList<Brand>());
        return searchResult;
    }


}
