import { SubmitHandler, useForm } from 'react-hook-form';
import { useNavigate } from 'react-router-dom';
import { CachePolicies, useFetch } from 'use-http';
import { useAuthContext } from '../../../contexts/AuthContext';
import { IAuthResponse } from '../../../interfaces/IAuthResponse';
import FormInput from '../../common/form-input/FormInput';

type Inputs = {
  Email: string;
  Password: string;
  rememberMe: boolean;
};

export default function LoginForm() {
  const navigate = useNavigate();
  const { loginUser } = useAuthContext();
  const { post, response } = useFetch<IAuthResponse>(
    `${process.env.REACT_APP_API_URL}/auth/authenticate`,
    { cachePolicy: CachePolicies.NO_CACHE }
  );

  const {
    register,
    handleSubmit,
    control,
    reset,
    formState: { errors },
  } = useForm<Inputs>({
    defaultValues: {
      Email: '',
      Password: '',
      rememberMe: false,
    },
    mode: 'onChange',
  });

  const onSubmit: SubmitHandler<Inputs> = async (data) => {
    // TODO: Add form validations

    const user = await post({
      email: data.Email,
      password: data.Password,
    });

    if (response.ok) {
      reset();
      loginUser(user);
      navigate('/');
    }
  };

  return (
    <form
      onSubmit={handleSubmit(onSubmit)}
      className="register-form"
      id="login-form">
      <FormInput
        control={control}
        name="Email"
        type="email"
        iconClasses="zmdi zmdi-account material-icons-name"
        rules={{ required: 'Email is required.' }}
      />

      <FormInput
        control={control}
        name="Password"
        type="password"
        iconClasses="zmdi zmdi-lock"
        rules={{ required: 'Password is required.' }}
      />

      <div className="form-group">
        <input
          type="checkbox"
          id="remember-me"
          className="agree-term"
          {...register('rememberMe')}
        />
        <label htmlFor="remember-me" className="label-agree-term">
          <span>
            <span></span>
          </span>
          Remember me
        </label>
      </div>
      <div className="form-group form-button">
        <input
          type="submit"
          name="signin"
          id="signin"
          className="btn_1"
          value="Log in"
        />
      </div>
    </form>
  );
}
