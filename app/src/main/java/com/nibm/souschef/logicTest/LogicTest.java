class StepNode{
    String instruction;
    int time; //in seconds
    StepNode previousStep;
    StepNode nextStep;

    public StepNode(String step, int stepTime){
        this.instruction = step;
        this.time = stepTime;
        this.previousStep = null;
        this.nextStep = null;
    }
}

class NodeMethods{
    StepNode head;

    public void newStep(String step, int time){
        StepNode newStep = new StepNode(step, time);
        if(head == null){
            this.head = newStep;
            return;
        }
        StepNode temp = head;
        while(temp.nextStep != null){
            temp = temp.nextStep;
        }
        newStep.previousStep = temp;
        temp.nextStep = newStep;
    }

    public void searchStep(){

    }
}

class LogicTest{
    
}