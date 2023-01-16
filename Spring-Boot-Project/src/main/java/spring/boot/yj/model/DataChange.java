package spring.boot.yj.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DataChange<T> {
private ChangeType changeType;
    private T data;

    public enum ChangeType{
        INSERT,
        UPDATE ,
        DELETE
    }
    
}
