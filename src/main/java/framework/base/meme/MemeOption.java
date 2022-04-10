package framework.base.meme;

enum MemeOption {
    ONE_POINT, // crossover
    SIMPLE_MEMEPLEX, // inheritance
    DAVISBIT_IO, DAVISBIT_NW, STEEPESTDESCENT_IO, STEEPESTDESCENT_NW, // local search
    BIT_MUTATION, // mutation
    TRANSGENERATIONAL_ELITISM, // replacement
    TOURNAMENT_SELECTION // selection
}
