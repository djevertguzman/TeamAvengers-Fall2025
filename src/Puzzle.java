
public class Puzzle {


    private String puzID;
    private String puzName;
    private String puzQuest;      // the question / prompt text
    private String puzAns;        // correct answer (string)
    private int puzMaxAtt;        // max attempts
    private int puzAttempt;       // attempts made so far
    private boolean isCompleted;

    // extra that i add that we needed for the game
    private String rewardItemIdOrName;  // e.g. "Rifle", "IT13", etc.
    private int penaltyHP;              // HP lost per wrong attempt

    // ===== Constructors =====


    public Puzzle(String puzID,
                  String puzName,
                  String puzQuest,
                  String puzAns,
                  String rewardItemIdOrName,
                  int penaltyHP) {

        this(puzID, puzName, puzQuest, puzAns,
                rewardItemIdOrName, penaltyHP, Integer.MAX_VALUE);
        // default: unlimited attempts, HP handles punishment
    }




    public Puzzle(String puzID,
                  String puzName,
                  String puzQuest,
                  String puzAns,
                  String rewardItemIdOrName,
                  int penaltyHP,
                  int puzMaxAtt) {

        this.puzID = puzID;
        this.puzName = puzName;
        this.puzQuest = puzQuest;
        this.puzAns = puzAns;
        this.rewardItemIdOrName = rewardItemIdOrName;
        this.penaltyHP = penaltyHP;

        this.puzMaxAtt = puzMaxAtt;
        this.puzAttempt = 0;
        this.isCompleted = false;
    }



    public String getPuzID() {
        return puzID;
    }

    public String getPuzName() {
        return puzName;
    }

    public String getQuestion() {
        return puzQuest;
    }

    public String getAnswer() {
        return puzAns;
    }

    public int getPenaltyHP() {
        return penaltyHP;
    }

    public int getPuzMaxAtt() {
        return puzMaxAtt;
    }

    public int getPuzAttempt() {
        return puzAttempt;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public String getRewardItemIdOrName() {
        return rewardItemIdOrName;
    }

// increments the attempt count
// checks if the answer is correct in a case-insensitive
// marks the puzzle as completed if the answer is correct
// returns true when correct and false when incorrect

// HP penalty is handled by the Game / Player code,
// which uses getPenaltyHP() whenever this method returns false.
    public boolean userAnswer(String userInput) {
        if (isCompleted) {
            return true; // already solved
        }

        puzAttempt++;

        if (userInput == null) {
            return false;
        }

        String cleaned = userInput.trim();
        String target = puzAns.trim();

        if (cleaned.equalsIgnoreCase(target)) {
            setComplete();
            return true;
        }

        // wrong answer:
        if (puzAttempt >= puzMaxAtt) {
            failPuzzle();
        }

        return false;
    }


    public Item despenseItem() {
        // Example if you later pass in a map of artifacts:
        // return artifactsMap.get(rewardItemIdOrName);
        return null;
    }

    // despenseItem() : Item
    // returns the reward item if needed
    // for now this method returns null
    // rewardItemIdOrName to look up the actual item in the artifacts map

    public void failPuzzle() {
        isCompleted = true;
    }


     // Marks the puzzle as successfully completed.

    public void setComplete() {
        isCompleted = true;
    }

    @Override
    public String toString() {
        return puzID + " - " + puzName;
    }
}
