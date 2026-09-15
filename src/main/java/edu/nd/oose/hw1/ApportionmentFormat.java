package edu.nd.oose.hw1;

import java.util.List;
import java.util.Map;

public interface ApportionmentFormat {

    String format(
            List<State> states,
            Map<State, Integer> apportionment
    );
}
