

//
// OPERATORS_USED: array of strings
//
// Keep track of the names of operators used in previous
//
let OPERATORS_USED = [];

//
// removeElement(string,array): array
//
// Returns a copy of the input array with
// the element e removed.
//
function removeElement(e, a) {
  let result = [];
  for (let i = 0; i < a.length; i++) {
    if (a[i] != e) {
      result.push(a[i]);
    }
  }
  return result;
}

//
// isMember(string,array): boolean
//
// Returns true if the element e is in the
// input array, a.
//
function isMember(e, a) {
  for (let i = 0; i < a.length; i++) {
    if (e == a[i]) {
      return true;
    }
  }
  return false;
}

//
// plan(array,array): [boolean,array,array]
//
// Given a currentState and a list of goals, and
// assuming a predefined list of OPERATORS, returns
// true, the end state, and a plan if the goals can
// be achieved; returns false, the currentState, and
// an invalid plan otherwise.
//
function plan(goals, currentState) {
  let nextState = [...currentState];
  currentPlan = [];
  let success = true;
  let i;
 display("beginning");
  for (i = 0; i < currentState.length; i++) {
    display("   " + currentState[i]);
  }
  for (i = 0; i < goals.length; i++) {
    [success, nextState, currentPlan] = achieveGoal(
      goals[i],
      nextState,
      currentPlan
    );
    if (!success) {
      console.log("unable to achieve goal " + goals[i]);
    }
  }
  if (!success) {
    console.log("plan not found");
  } else {
    display("ending");
    for (i = 0; i < nextState.length; i++) {
      console.log("   " + nextState[i]);
    }
  }
  return [success, nextState, currentPlan];
}

//
// achieveGoal(string,array,array): [boolean,array,array]
//
// If goal is in the currentState, returns immediately;
// otherwise, operators that could achieve the goal are
// selected and each selected is attempted.
//
// Returns an updated state and plan if an attempt succeeds;
// returns the current state and plan if all fail.
//
function achieveGoal(goal, currentState, currentPlan) {
  let nextState = [...currentState];
  let nextPlan = [...currentPlan];
  if (isMember(goal, nextState)) {
    return [true, currentState, currentPlan];
  } else {
    let selections = selectOperators(goal);
    let success = false;
    for (let i = 0; i < selections.length; i++) {
      if (!success) {
        [success, nextState, nextPlan] = applyOperator(
          selections[i],
          nextState,
          nextPlan
        );
      }
    }
    if (success) {
      return [true, nextState, nextPlan];
    } else {
      return [false, currentState, currentPlan];
    }
  }
}

//
// selectOperators(string): array
//
// Finds all operators containing the goal in the
// additions list.
//
function selectOperators(goal) {
  let selections = [];
  let usedSelections = [];
  let unusedSelections = [];

  for (let i = 0; i < OPERATORS.length; i++) {
    if (isMember(goal, OPERATORS[i].additions)) {
      selections.push(OPERATORS[i]);
    }
  }

  for (let i = 0; i < selections.length; i++) {
    if (isMember(selections[i].name, OPERATORS_USED)) {
      usedSelections.push(selections[i]);
    } else {
      unusedSelections.push(selections[i]);
    }
  }

  return unusedSelections.concat(usedSelections);
}
function applyOperator(operator, currentState, currentPlan) {
  let nextState = [...currentState];
  let nextPlan = [...currentPlan];
  let success = true;

  for (let i = 0; i < operator.preconditions.length; i++) {
    if (success) {
      [success, nextState, nextPlan] = achieveGoal(
        operator.preconditions[i],
        nextState,
        nextPlan
      );
    }
  }
  // If the preconditions are solved, execute the operator
  // by deleting its deletions and adding its additions to
  // the state.
  if (success) {
    display(operator.name);
    nextPlan.push(operator.name);
    if (operator.deletions.length > 0) {
      console.log("   deleting ");
    }
    for (i = 0; i < operator.deletions.length; i++) {
      console.log("       " + operator.deletions[i]);
      nextState = removeElement(operator.deletions[i], nextState);
    }
    if (operator.additions.length > 0) {
      console.log("   adding ");
    }
    for (i = 0; i < operator.additions.length; i++) {
      console.log("       " + operator.additions[i]);
      nextState.push(operator.additions[i]);
    }
    OPERATORS_USED.push(operator.name);
    return [true, nextState, nextPlan];
  } else {
    return [false, currentState, currentPlan];
  }
}

function display(message) {
  let type;
  let sec = document.querySelector("#result");
  if (message === "beginning" || message === "ending") {
    type = "h1";
  } else {
    type = "p";
  }
  let tag = document.createElement(type);
  tag.textContent = message;
  sec.appendChild(tag);
  console.log(message);
}

/**********************************************
 *  STORY OPERATORS: "She Gets Ready and Leaves"
 **********************************************/

let STORY_OPERATORS = [
  {
    name: "she sends a text message",
    preconditions: [],
    additions: ["textSent"],
    deletions: []
  },
  {
    name: "phone lights up with a response",
    preconditions: ["textSent"],
    additions: ["phoneResponded"],
    deletions: []
  },
  {
    name: "she walks toward the mirror",
    preconditions: ["phoneResponded"],
    additions: ["approachingMirror"],
    deletions: []
  },
  {
    name: "she applies her lipstick",
    preconditions: ["approachingMirror"],
    additions: ["lipstickApplied"],
    deletions: []
  },
  {
    name: "she grabs her purse from the hook",
    preconditions: ["lipstickApplied"],
    additions: ["purseGrabbed"],
    deletions: []
  },
  {
    name: "he rings doorbell",
    preconditions: ["purseGrabbed"],
    additions: ["doorbellRang"],
    deletions: []
  },
  {
    name: "her heart beats",
    preconditions: ["doorbellRang"],
    additions: ["heartRacing"],
    deletions: []
  },
  {
    name: "she turns door knob",
    preconditions: ["heartRacing"],
    additions: ["doorOpened"],
    deletions: []
  },
  {
    name: "they walk to the car",
    preconditions: ["doorOpened"],
    additions: ["atCar"],
    deletions: []
  }
];

let OPERATORS = STORY_OPERATORS;

// Call plan once
plan(
  ["atCar"], // goal
  []         // currentState
);

// Call plan a second time
plan(
  ["atCar"],
  []
);
