package com.example;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class Suggestion {

    String[] dictionary = {
            "noah",
            "liam",
            "mason",
            "jacob",
            "william",
            "ethan",
            "james",
            "alexander",
            "michael",
            "benjamin",
            "elijah",
            "daniel",
            "logan",
            "matthew",
            "lucas",
            "jackson",
            "david",
            "oliver",
            "joseph",
            "gabriel",
            "samuel",
            "carter",
            "anthony",
            "john",
            "dylan",
            "luke",
            "henry",
            "andrew",
            "isaac",
            "christopher",
            "joshua",
            "wyatt",
            "sebastian",
            "owen",
            "caleb",
            "nathan",
            "ryan",
            "jack",
            "hunter",
            "levi",
            "christian",
            "julian",
            "landon",
            "grayson",
            "jonathan",
            "isaiah",
            "charles",
            "thomas",
            "aaron",
            "connor",
            "jeremiah",
            "cameron",
            "josiah",
            "adrian",
            "jordan",
            "nicholas",
            "robert",
            "angel",
            "hudson",
            "lincoln",
            "evan",
            "dominic",
            "austin",
            "gavin",
            "nolan",
            "parker",
            "adam",
            "chase",
            "cooper",
            "kevin",
            "jose",
            "tyler",
            "brandon",
            "asher",
            "mateo",
            "jason",
            "zachary",
            "carson",
            "xavier",
            "leo",
            "ezra",
            "bentley",
            "sawyer",
            "blake",
            "nathaniel",
            "ryder",
            "theodore",
            "elias",
            "tristan",
            "roman",
            "leonardo",
            "camden",
            "brody",
            "luis",
            "miles",
            "micah",
            "vincent",
            "justin",
            "maxwell",
            "juan",
            "cole",
            "damian",
            "carlos",
            "max",
            "harrison",
            "weston",
            "brantley",
            "axel",
            "diego",
            "abel",
            "wesley",
            "santiago",
            "jesus",
            "silas",
            "giovanni",
            "bryce",
            "alex",
            "everett",
            "george",
            "eric",
            "ivan",
            "emmett",
            "ashton",
            "kingston",
            "jonah",
            "jameson",
            "kai",
            "maddox",
            "timothy",
            "ezekiel",
            "emmanuel",
            "hayden",
            "antonio",
            "bennett",
            "steven",
            "richard",
            "jude",
            "luca",
            "edward",
            "joel",
            "victor",
            "miguel",
            "malachi",
            "king",
            "patrick",
            "kaleb",
            "bryan",
            "alan",
            "marcus",
            "preston",
            "abraham",
            "calvin",
            "colin",
            "bradley",
            "jeremy",
            "kyle",
            "graham",
            "grant",
            "jesse",
            "alejandro",
            "oscar",
            "aidan",
            "tucker",
            "avery",
            "brian",
            "matteo",
            "riley",
            "august",
            "mark",
            "brady",
            "kenneth",
            "paul",
            "nicolas",
            "beau",
            "dean",
            "jake",
            "peter",
            "elliot",
            "finn",
            "derek",
            "sean",
            "elliott",
            "jasper",
            "lorenzo",
            "omar",
            "beckett",
            "rowan",
            "gael",
            "corbin",
            "waylon",
            "myles",
            "tanner",
            "jorge",
            "javier",
            "zion",
            "andres",
            "charlie",
            "paxton",
            "brooks",
            "zane",
            "simon",
            "judah",
            "griffin",
            "cody",
            "gunner",
            "dawson",
            "israel",
            "gage",
            "messiah",
            "stephen",
            "francisco",
            "clayton",
            "chance",
            "eduardo",
            "spencer",
            "lukas",
            "damien",
            "dallas",
            "conner",
            "travis",
            "knox",
            "raymond",
            "peyton",
            "devin",
            "felix",
            "cash",
            "fernando",
            "keegan",
            "garrett",
            "rhett",
            "ricardo",
            "martin",
            "reid",
            "seth",
            "andre",
            "cesar",
            "titus",
            "donovan",
            "manuel",
            "mario",
            "milo",
            "archer",
            "jeffrey",
            "holden",
            "arthur",
            "rafael",
            "shane",
            "lane",
            "louis",
            "angelo",
            "remington",
            "troy",
            "emerson",
            "hector",
            "emilio",
            "anderson",
            "trevor",
            "phoenix",
            "walter",
            "johnathan",
            "johnny",
            "edwin",
            "julius",
            "barrett",
            "leon",
            "tyson",
            "tobias",
            "edgar",
            "dominick",
            "marshall",
            "marco",
            "joaquin",
            "dante",
            "andy",
            "cruz",
            "ali",
            "finley",
            "dalton",
            "gideon",
            "reed",
            "sergio",
            "jett",
            "ronan",
            "cohen",
            "colt",
            "erik",
            "trenton",
            "jared",
            "walker",
            "alexis",
            "nash",
            "gregory",
            "emanuel",
            "killian",
            "allen",
            "desmond",
            "shawn",
            "grady",
            "quinn",
            "frank",
            "fabian",
            "dakota",
            "roberto",
            "beckham",
            "major",
            "skyler",
            "nehemiah",
            "drew",
            "muhammad",
            "kendrick",
            "pedro",
            "orion",
            "aden",
            "ruben",
            "clark",
            "noel",
            "porter",
            "solomon",
            "romeo",
            "rory",
            "leland",
            "abram",
            "derrick",
            "gunnar",
            "prince",
            "brendan",
            "pablo",
            "jay",
            "jensen",
            "esteban",
            "drake",
            "warren",
            "ismael",
            "ari",
            "russell",
            "bruce",
            "finnegan",
            "marcos",
            "jayson",
            "theo",
            "phillip",
            "dexter",
            "armando",
            "braden",
            "corey",
            "gerardo",
            "ellis",
            "malcolm",
            "tate",
            "zachariah",
            "chandler",
            "milan",
            "keith",
            "danny",
            "damon",
            "enrique",
            "jonas",
            "kane",
            "princeton",
            "hugo",
            "ronald",
            "philip",
            "ibrahim",
            "maximilian",
            "lawson",
            "harvey",
            "albert",
            "donald",
            "raul",
            "franklin",
            "hendrix",
            "odin",
            "brennan",
            "jamison",
            "dillon",
            "brock",
            "colby",
            "alec",
            "julio",
            "scott",
            "sullivan",
            "rodrigo",
            "taylor",
            "rocco",
            "royal",
            "pierce",
            "augustus",
            "benson",
            "moses",
            "cyrus",
            "davis",
            "khalil",
            "moises",
            "nikolai",
            "mathew",
            "keaton",
            "francis",
            "quentin",
            "jaime",
            "lennox",
            "atlas",
            "jerry",
            "ahmed",
            "saul",
            "sterling",
            "dennis",
            "lawrence",
            "darius",
            "eden",
            "tony",
            "dustin",
            "chris",
            "mohammed",
            "nixon",
            "emma",
            "olivia",
            "sophia",
            "ava",
            "isabella",
            "mia",
            "abigail",
            "emily",
            "charlotte",
            "harper",
            "madison",
            "amelia",
            "elizabeth",
            "sofia",
            "evelyn",
            "chloe",
            "ella",
            "grace",
            "victoria",
            "aubrey",
            "scarlett",
            "addison",
            "lillian",
            "natalie",
            "hannah",
            "brooklyn",
            "alexa",
            "zoe",
            "penelope",
            "leah",
            "audrey",
            "savannah",
            "allison",
            "samantha",
            "nora",
            "skylar",
            "camila",
            "anna",
            "ariana",
            "ellie",
            "claire",
            "violet",
            "stella",
            "sadie",
            "gabriella",
            "lucy",
            "kennedy",
            "sarah",
            "madelyn",
            "eleanor",
            "kaylee",
            "caroline",
            "hazel",
            "hailey",
            "genesis",
            "kylie",
            "autumn",
            "piper",
            "maya",
            "mackenzie",
            "bella",
            "eva",
            "naomi",
            "aubree",
            "aurora",
            "melanie",
            "lydia",
            "brianna",
            "ruby",
            "katherine",
            "ashley",
            "alice",
            "cora",
            "julia",
            "madeline",
            "faith",
            "annabelle",
            "alyssa",
            "isabelle",
            "vivian",
            "gianna",
            "clara",
            "reagan",
            "alexandra",
            "hadley",
            "sophie",
            "london",
            "elena",
            "kimberly",
            "bailey",
            "maria",
            "luna",
            "willow",
            "jasmine",
            "kinsley",
            "valentina",
            "kayla",
            "delilah",
            "andrea",
            "natalia",
            "lauren",
            "morgan",
            "sydney",
            "mary",
            "jade",
            "trinity",
            "josephine",
            "jocelyn",
            "emery",
            "adeline",
            "ariel",
            "lilly",
            "paige",
            "molly",
            "emilia",
            "kendall",
            "melody",
            "isabel",
            "brooke",
            "mckenzie",
            "nicole",
            "payton",
            "margaret",
            "athena",
            "amy",
            "valeria",
            "sara",
            "angelina",
            "gracie",
            "rose",
            "rachel",
            "juliana",
            "valerie",
            "reese",
            "elise",
            "eliza",
            "catherine",
            "cecilia",
            "genevieve",
            "daisy",
            "harmony",
            "vanessa",
            "adriana",
            "presley",
            "rebecca",
            "julianna",
            "michelle",
            "arabella",
            "summer",
            "callie",
            "kaitlyn",
            "lila",
            "daniela",
            "alana",
            "esther",
            "gabrielle",
            "jessica",
            "stephanie",
            "tessa",
            "ana",
            "alexandria",
            "nova",
            "anastasia",
            "iris",
            "marley",
            "fiona",
            "angela",
            "giselle",
            "kate",
            "lola",
            "lucia",
            "juliette",
            "georgia",
            "hope",
            "cali",
            "vivienne",
            "katelyn",
            "juliet",
            "maggie",
            "delaney",
            "camille",
            "leila",
            "mckenna",
            "noelle",
            "josie",
            "jennifer",
            "melissa",
            "gabriela",
            "allie",
            "eloise",
            "jacqueline",
            "brynn",
            "evangeline",
            "paris",
            "olive",
            "rosalie",
            "kali",
            "gemma",
            "lena",
            "adelaide",
            "alessandra",
            "miranda",
            "haley",
            "june",
            "harley",
            "lucille",
            "talia",
            "phoebe",
            "jane",
            "elaina",
            "adrianna",
            "ruth",
            "miriam",
            "diana",
            "mariana",
            "danielle",
            "jenna",
            "shelby",
            "nina",
            "madeleine",
            "chelsea",
            "joanna",
            "jada",
            "lexi",
            "katie",
            "fatima",
            "lilah",
            "amanda",
            "daniella",
            "alexia",
            "kathryn",
            "selena",
            "laura",
            "annie",
            "catalina",
            "sloane",
            "haven",
            "christina",
            "amber",
            "erin",
            "alison",
            "ainsley",
            "kendra",
            "heidi",
            "kelsey",
            "nadia",
            "cheyenne",
            "arielle",
            "lana",
            "ada",
            "allyson",
            "felicity",
            "kira",
            "alicia",
            "veronica",
            "esmeralda",
            "leslie",
            "aspen",
            "camilla",
            "scarlet",
            "daphne",
            "bianca",
            "mckinley",
            "carmen",
            "megan",
            "skye",
            "elsie",
            "carly",
            "mallory",
            "annabella",
            "elle",
            "zara",
            "april",
            "gwendolyn",
            "annalise",
            "tatum",
            "serena",
            "dahlia",
            "macy",
            "briana",
            "freya",
            "helen",
            "bethany",
            "leia",
            "harlow",
            "angelica",
            "marilyn",
            "viviana",
            "francesca",
            "carolina",
            "jillian",
            "joy",
            "abby",
            "malaysia",
            "kaia",
            "bristol",
            "lorelei",
            "alejandra",
            "justice",
            "julie",
            "marlee",
            "brittany",
            "amara",
            "karina",
            "thea",
            "luciana",
            "aubrie",
            "janelle",
            "leighton",
            "eve",
            "millie",
            "kelly",
            "lacey",
            "willa",
            "sylvia",
            "melany",
            "elisa",
            "elsa",
            "raven",
            "holly",
            "aisha",
            "kyra",
            "tiffany",
            "jamie",
            "celeste",
            "lilian",
            "priscilla",
            "karen",
            "lauryn",
            "alanna",
            "kara",
            "karla",
            "cassandra",
            "evie"
    };

    LevenshteinDistance lsd;

    public Suggestion() {
        lsd = new LevenshteinDistance();
    }

    public List<String> getSuggestion(String input) {

        List<Double> distList = new ArrayList<Double>();

        double minDist = 1000.0;

        for (int i = 0; i < dictionary.length; i++) {
            //double computedDist = lsd.computeLevenshteinDistance(dictionary[i], input);
            double computedDist = lsd.execute(dictionary[i], input);
            distList.add(computedDist);
            if (i == 0) {
                minDist = computedDist;
            } else {
                if (computedDist < minDist) {
                    minDist = computedDist;
                }
            }
        }

        List<String> suggetedResult = new ArrayList<String>();

        for (int i = 0; i < distList.size(); i++) {
            double newNumber = distList.get(i);
            boolean isMin = newNumber == minDist;
            if (isMin) {
                suggetedResult.add(dictionary[i]);
            }
        }

        return suggetedResult;
    }

    public double getDistance(CharSequence lhs, CharSequence rhs) {
        double dist = lsd.computeLevenshteinDistance(lhs, rhs);
        return dist;
    }

    public double getPositionX(char a) {
        double position = lsd.getPositionX(a);
        return position;
    }

    public double getPositionY(char a) {
        double position = lsd.getPositionY(a);
        return position;
    }

    public double getPlenty(char a, char b) {
        double distX = getPositionX(a) - getPositionX(b);
        double distY = getPositionY(a) - getPositionY(b);

        double ret = Math.sqrt(Math.pow(distX, 2) + Math.pow(distY, 2));

        ret = Math.round(ret);

        return ret;
    }

    public class LevenshteinDistance {

        String line1 = "qwertyuiop", line2 = "asdfghjkl", line3 = "zxcvbnm";
        double nomalize = Math.sqrt(Math.pow(3,2) + Math.pow(8.5, 2));

        double substitutionCost;
        double deletionCost = 1.0;
        double insertionCost = 1.0;

        private double minimum(double a, double b, double c) {
            return Math.min(Math.min(a, b), c);
        }

        public double computeLevenshteinDistance(CharSequence lhs, CharSequence rhs) {

            int lhsLength = lhs.length();
            int rhsLength = rhs.length();

            double[][] distance = new double[lhsLength + 1][rhsLength + 1];

            for (int i = 0; i <= lhsLength; i++)
                distance[i][0] = i;
            for (int j = 1; j <= rhsLength; j++)
                distance[0][j] = j;

            for (int i = 1; i <= lhsLength; i++) {
                for (int j = 1; j <= rhsLength; j++) {
                    //substitutionCost = lhs.charAt(i - 1) == rhs.charAt(j - 1) ? 0.0 : 1.0;
                    substitutionCost = getPenelty(lhs.charAt(i - 1), rhs.charAt(j - 1));
                    distance[i][j] = minimum(
                            distance[i - 1][j] + deletionCost, // deletion
                            distance[i][j - 1] + insertionCost, // insertion
                            distance[i - 1][j - 1] + substitutionCost); // substitution
                }
            }

            return distance[lhsLength][rhsLength];
        }

        public double getPositionX(char a) {

            double positionX = 0.0;
            if (line1.indexOf(a) != -1) {
                positionX = line1.indexOf(a);
            } else {
                if (line2.indexOf(a) != -1) {
                    positionX = line2.indexOf(a) + 0.5;
                } else {
                    if (line3.indexOf(a) != -1) {
                        positionX = line3.indexOf(a) + 1.5;
                    }
                }
            }

            return positionX;
        }

        public double getPositionY(char a) {

            double positionY = 0.0;

            if (line1.indexOf(a) != -1) {
                positionY = 0.0;
            } else {
                if (line2.indexOf(a) != -1) {
                    positionY = 1.0;
                } else {
                    if (line3.indexOf(a) != -1) {
                        positionY = 2.0;
                    }
                }
            }

            return positionY;
        }

        public double getPenelty(char a, char b) {
            double distX = getPositionX(a) - getPositionX(b);
            double distY = getPositionY(a) - getPositionY(b);

            double ret = Math.sqrt(Math.pow(distX, 2) + Math.pow(distY, 2));

            ret = Math.round(ret);

            return ret;
        }

        public int execute(String source, String target) {

            int insertCost = 2;
            int deleteCost = 2;
            int swapCost = 1;

            if (source.length() == 0) {
                return target.length() * insertCost;
            }
            if (target.length() == 0) {
                return source.length() * deleteCost;
            }
            int[][] table = new int[source.length()][target.length()];
            Map<Character, Integer> sourceIndexByCharacter = new HashMap<Character, Integer>();
            if (source.charAt(0) != target.charAt(0)) {
                table[0][0] = Math.min((int) getPenelty(source.charAt(0), target.charAt(0)), deleteCost + insertCost);
            }
            sourceIndexByCharacter.put(source.charAt(0), 0);
            for (int i = 1; i < source.length(); i++) {
                int deleteDistance = table[i - 1][0] + deleteCost;
                int insertDistance = (i + 1) * deleteCost + insertCost;
                int matchDistance = i * deleteCost
                        + (int) getPenelty(source.charAt(i), target.charAt(0));
                table[i][0] = Math.min(Math.min(deleteDistance, insertDistance),
                        matchDistance);
            }
            for (int j = 1; j < target.length(); j++) {
                int deleteDistance = (j + 1) * insertCost + deleteCost;
                int insertDistance = table[0][j - 1] + insertCost;
                int matchDistance = j * insertCost
                        + (int) getPenelty(source.charAt(0), target.charAt(j));
                table[0][j] = Math.min(Math.min(deleteDistance, insertDistance),
                        matchDistance);
            }
            for (int i = 1; i < source.length(); i++) {
                int maxSourceLetterMatchIndex = source.charAt(i) == target.charAt(0) ? 0
                        : -1;
                for (int j = 1; j < target.length(); j++) {
                    Integer candidateSwapIndex = sourceIndexByCharacter.get(target
                            .charAt(j));
                    int jSwap = maxSourceLetterMatchIndex;
                    int deleteDistance = table[i - 1][j] + deleteCost;
                    int insertDistance = table[i][j - 1] + insertCost;
                    int matchDistance = table[i - 1][j - 1];
                    if (source.charAt(i) != target.charAt(j)) {
                        matchDistance += (int) getPenelty(source.charAt(i), target.charAt(j));
                    } else {
                        maxSourceLetterMatchIndex = j;
                    }
                    int swapDistance;
                    if (candidateSwapIndex != null && jSwap != -1) {
                        int iSwap = candidateSwapIndex;
                        int preSwapCost;
                        if (iSwap == 0 && jSwap == 0) {
                            preSwapCost = 0;
                        } else {
                            preSwapCost = table[Math.max(0, iSwap - 1)][Math.max(0, jSwap - 1)];
                        }
                        swapDistance = preSwapCost + (i - iSwap - 1) * deleteCost
                                + (j - jSwap - 1) * insertCost + swapCost;
                    } else {
                        swapDistance = Integer.MAX_VALUE;
                    }
                    table[i][j] = Math.min(Math.min(Math
                            .min(deleteDistance, insertDistance), matchDistance), swapDistance);
                }
                sourceIndexByCharacter.put(source.charAt(i), i);
            }
            return table[source.length() - 1][target.length() - 1];
        }
    }
}
